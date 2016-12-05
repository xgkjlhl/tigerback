package tiger.web.api.controller.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tiger.biz.portal.support.PortalUIManager;
import tiger.common.dal.enums.PortalPositionEnum;
import tiger.core.base.BaseResult;
import tiger.core.enums.ErrorCodeEnum;
import tiger.web.api.controller.base.BaseController;

/**
 * Created by Jaric Liao on 2016/1/27.
 */
@RestController
@ResponseBody
public class PortalController extends BaseController {

    @Autowired
    private PortalUIManager portalUIManager;

    /**
     * 获取对应位置的图片信息
     * @param position
     * @return
     */
    @RequestMapping(value = "/portal/ui", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult getPicture(@RequestParam("position") String position){
        PortalPositionEnum positionEnum = PortalPositionEnum.getEnumByCode(position);
        if ( positionEnum == null ) return new BaseResult(ErrorCodeEnum.ILLEGAL_PARAMETER);
        else return new BaseResult(portalUIManager.getPictures(positionEnum));
    }
}
