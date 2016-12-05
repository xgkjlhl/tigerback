package tiger.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tiger.common.dal.dataobject.PortalUIDO;
import tiger.common.dal.dataobject.example.PortalUIExample;
import tiger.common.dal.enums.PortalPositionEnum;
import tiger.common.dal.persistence.PortalUIMapper;
import tiger.core.domain.PortalUIRelateDomain;
import tiger.core.domain.convert.PortalUIConvert;
import tiger.core.service.PortalUIService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jaric Liao on 2016/1/23.
 */
@Service
public class PortalUIServiceImpl implements PortalUIService {

    @Autowired
    PortalUIMapper portalUIMapper;

    /**
     * @see PortalUIService#addPicture(PortalUIRelateDomain)
     */
    @Override
    public Boolean addPicture(PortalUIRelateDomain domain){
        PortalUIDO portalUIDO = PortalUIConvert.convert2DO(domain);
        return portalUIMapper.insert(portalUIDO)>0;
    }

    /**
     * @see PortalUIService#readPicture(Long)
     */
    @Override
    public PortalUIRelateDomain readPicture(Long id){
        PortalUIDO portalUIDO = portalUIMapper.selectByPrimaryKey(id);
        return PortalUIConvert.convert2Domain(portalUIDO);
    }

    /**
     * @see PortalUIService#getPicturesByExample(PortalUIExample)
     */
    @Override
    public List<PortalUIRelateDomain> getPicturesByExample(PortalUIExample example){
        List<PortalUIDO> pictures = portalUIMapper.selectByExample(example);
        List<PortalUIRelateDomain> results = new ArrayList<>();
        for ( PortalUIDO portalUIDO : pictures ) results.add(PortalUIConvert.convert2Domain(portalUIDO));
        return results;
    }

    /**
     * @see PortalUIService#deletePicture(Long)
     */
    @Override
    public Boolean deletePicture(Long id){
        return portalUIMapper.deleteByPrimaryKey(id)>0;
    }

    /**
     * @see PortalUIService#getPictureCountByExample(PortalUIExample)
     */
    @Override
    public int getPictureCountByExample(PortalUIExample example) {
        return portalUIMapper.countByExample(example);
    }

    /**
     * @see PortalUIService#updateByExampleSelective(PortalUIRelateDomain)
     */
    @Override
    public Boolean updateByExampleSelective(PortalUIRelateDomain domain) {
        PortalUIExample example = new PortalUIExample();
        example.createCriteria().andIdEqualTo(domain.getId());
        PortalUIDO portalUIDO = PortalUIConvert.convert2DO(domain);
        return portalUIMapper.updateByExampleSelective(portalUIDO, example)>0;
    }

    /**
     * @see PortalUIService#resortPictureRank(List, int)
     */
    @Override
    public Boolean resortPictureRank(List<Long> ids, int adjustValue) {
        return portalUIMapper.resortRank(ids,adjustValue)==ids.size();
    }
}
