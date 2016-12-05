package tiger.web.mng.controller.portal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tiger.biz.mng.attach.support.AttachManager;
import tiger.biz.mng.portal.support.PortalUIManager;
import tiger.core.base.BaseResult;
import tiger.core.domain.PortalUIRelateDomain;
import tiger.web.mng.controller.base.BaseController;
import tiger.web.mng.form.portal.UIEidtForm;
import tiger.web.mng.form.portal.UIRelateForm;

import javax.validation.Valid;

/**
 * Created by Jaric Liao on 2016/1/21.
 */

@Controller
public class PortalUIController extends BaseController {

    private Logger logger = Logger.getLogger(PortalUIController.class);

    @Autowired
    private PortalUIManager portalUIManager;
    @Autowired
    private AttachManager attachManager;


    /**
     * 获取全部图片列表
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/portalConfig", method = RequestMethod.GET)
    public String portalConfig(@ModelAttribute("model")ModelMap model) {
        model.addAttribute("pictureList", portalUIManager.getPictures());
        return "/contentManagement/portalConfig";
    }

    /**
     * 上传图片文件
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/portalConfig/file", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<Boolean> uploadPictureFile(MultipartFile file){
        return new BaseResult(attachManager.uploadPublicAttach(file));
    }

    /**
     * 上传图片
     *
     * @param uiRelateForm
     * @return
     */
    @RequestMapping(value = "/portalConfig", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<Boolean> uploadPicture(@Valid UIRelateForm uiRelateForm,
                                             BindingResult bindingResult){
        PortalUIRelateDomain portalUIRelateDomain = uiRelateForm.convert2Domain();
        return new BaseResult(portalUIManager.relateAttach(portalUIRelateDomain));
    }

    /**
     * 根据ID获取图片
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/portalConfig/{id}", method = RequestMethod.GET)
    public String getPicture(@PathVariable("id") Long id,
                             @ModelAttribute("model") ModelMap model) {
        model.addAttribute("picture", portalUIManager.getPictureById(id));
        model.addAttribute("position", portalUIManager.getPositionEnums());
        return "/contentManagement/modal/editPicture";
    }

    /**
     * 根据ID更新图片信息
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/portalConfig/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public BaseResult<Boolean> update(@PathVariable("id") Long id,
                          @RequestBody @Valid UIEidtForm form,
                          BindingResult bindingResult) {
        PortalUIRelateDomain pictureDomain = form.convert2Domain();
        pictureDomain.setId(id);
        return new BaseResult(portalUIManager.updatePicture(pictureDomain));
    }

    /**
     * 根据ID设置置顶图片
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/portalConfig/{id}", method = RequestMethod.PUT, params = "operation=top")
    @ResponseBody
    public boolean setTop(@PathVariable("id") Long id){
        return portalUIManager.setTopPicture(id);
    }

    /**
     * 获取位置信息
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/portalConfig/position", method = RequestMethod.GET)
    public String getPicture(@ModelAttribute("model") ModelMap model) {
        model.addAttribute("position", portalUIManager.getPositionEnums());
        return "/contentManagement/modal/uploadPicture";
    }

    /**
     * 根据ID删除图片
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/portalConfig/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public BaseResult<Boolean> deletePicture(@PathVariable("id") Long id) {
        if (logger.isInfoEnabled()) {
            logger.info("管理员 [" + currentAccount() + "] 删除图片 [" + id + "]");
        }
        return new BaseResult(portalUIManager.deletePictrue(id));
    }

    // private method




}
