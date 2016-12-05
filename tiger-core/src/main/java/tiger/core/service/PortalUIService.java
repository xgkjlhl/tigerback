package tiger.core.service;

import tiger.common.dal.dataobject.PortalUIDO;
import tiger.common.dal.dataobject.example.PortalUIExample;
import tiger.common.dal.enums.PortalPositionEnum;
import tiger.core.domain.PortalUIRelateDomain;

import java.util.List;

/**
 * Created by Jaric Liao on 2016/1/23.
 */
public interface PortalUIService {

    // 关联附件与图片，增加图片记录
    Boolean addPicture(PortalUIRelateDomain domain);

    // 获取portal页面图片记录
    PortalUIRelateDomain readPicture(Long id);

    //删除portal页面图片记录
    Boolean deletePicture(Long id);

    // 根据Example获取页面图片
    List<PortalUIRelateDomain> getPicturesByExample(PortalUIExample example);

    // 获取当前条件下共有多少图片
    int getPictureCountByExample(PortalUIExample example);

    // 根据ID更新页面图片记录
    Boolean updateByExampleSelective(PortalUIRelateDomain record);

    // 根据ID调整页面图片记录的位置排序
    Boolean resortPictureRank(List<Long>ids, int adjustValue);
}
