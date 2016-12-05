/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.controller.attach;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tiger.biz.attach.support.AttachManager;
import tiger.common.dal.annotation.LoginRequired;
import tiger.common.dal.enums.AttachTypeEnum;
import tiger.core.base.BaseResult;
import tiger.core.domain.AttachDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;
import tiger.core.service.AttachService;
import tiger.web.api.controller.base.BaseController;
import tiger.web.api.form.attach.AttachForm;
import tiger.web.api.form.attach.QiniuAttachForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class AttachController.
 *
 * @author alfred.yx
 * @version v 0.1 Oct 2, 2015 10:12:42 PM alfred Exp $
 */
@RestController
@EnableAutoConfiguration
public class QiniuAttachController extends BaseController {
    @Autowired
    AttachService attachService;

    @Autowired
    AttachManager qiniuAttachManager;

    /**
     * 获取附件.
     *
     * @param id the id
     * @return the attach by id
     */
    @RequestMapping(value = "/attach/{id}", method = RequestMethod.GET)
    @ResponseBody
    @LoginRequired
    public BaseResult<Object> getAttachById(@PathVariable long id) {
        if (!attachService.isExist(id)) {
            return new BaseResult<>(ErrorCodeEnum.NOT_FOUND, false);
        }
        if (!attachService.isOwner(id, currentAccount().getId())) {
            return new BaseResult<>(ErrorCodeEnum.UNAUTHORIZED, false);
        }
        return new BaseResult<>(attachService.getAttachWithSignedUrlById(id));
    }

    /**
     * 获取预览附件
     *
     * @param id the id
     * @return the attach by id
     */
    @RequestMapping(value = "/attach/{id}", method = RequestMethod.GET, params = "action=preview")
    @ResponseBody
    @LoginRequired
    public BaseResult<Object> getPreviewAttachById(@PathVariable long id,
                                                   @RequestParam("method") String method) {
        if (!attachService.isExist(id)) {
            return new BaseResult<>(ErrorCodeEnum.NOT_FOUND, false);
        }
        if (!attachService.isOwner(id, currentAccount().getId())) {
            return new BaseResult<>(ErrorCodeEnum.UNAUTHORIZED, false);
        }
        return new BaseResult<>(qiniuAttachManager.previewAttach(id, method));
    }

    /**
     * 创建附件
     *
     * @param attachForm    the id
     * @param bindingResult the binding result
     * @return the base result
     */
    @RequestMapping(value = "/attach", method = RequestMethod.POST)
    @ResponseBody
    @LoginRequired
    public BaseResult<Object> createAttach(@RequestBody @Valid AttachForm attachForm,
                                           BindingResult bindingResult) {
        if (!isValidAttachType(attachForm.getAttachType())) {
            return new BaseResult<>(ErrorCodeEnum.ILLEGAL_PARAMETER_VALUE, "无效的附件类型");
        }
        return new BaseResult<>(attachService.getQiniuUploadToken(attachForm.convert2Domain(),
                currentAccount().getId()));
    }

    /**
     * 七牛云callback api
     */
    @RequestMapping(value = "/qiniuattach", method = RequestMethod.POST)
    @ResponseBody
    public Object qiniuCallBack(@RequestHeader("Authorization") String Authorization,
                                @RequestBody String callbackBody,
                                @RequestHeader("Content-Type") String callbackContentType,
                                HttpServletRequest request,
                                HttpServletResponse response,
                                @ModelAttribute QiniuAttachForm qiniuAttachForm) {
        try {
            String contentType = request.getContentType();
            AttachDomain attach = attachService.qiniuCallback(qiniuAttachForm.convert2Domain(), Authorization, callbackBody, contentType);
            // 将结果保存到七牛云要求的返回格式中
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("key", attach.getUrl());
            resultMap.put("payload", new BaseResult<>(attach));
            return resultMap;
        } catch (TigerException t) {
            response.setStatus(400);
            return new BaseResult<>(t);
        }
    }

    /**
     * 删除附件
     *
     * @param id the id
     * @return the base result
     */
    @RequestMapping(value = "/attach/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @LoginRequired
    public BaseResult<Boolean> deleteAttachById(@PathVariable long id) {
        if (!attachService.isExist(id)) {
            return new BaseResult<>(ErrorCodeEnum.NOT_FOUND, false);
        }
        if (!attachService.isOwner(id, currentAccount().getId())) {
            return new BaseResult<>(ErrorCodeEnum.UNAUTHORIZED, false);
        }
        return new BaseResult<>(attachService.deleteAttachById(id));
    }


    // ～ Private method
    private boolean isValidAttachType(String attachType) {
        for (AttachTypeEnum attachTypeEnum : AttachTypeEnum.values()) {
            if (attachTypeEnum.getCode().equals(attachType)) {
                return true;
            }
        }
        return false;
    }
}
