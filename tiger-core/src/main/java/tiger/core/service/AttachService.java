/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service;

import tiger.common.dal.enums.AttachTypeEnum;
import tiger.common.dal.query.AttachQuery;
import tiger.core.domain.AttachDomain;
import tiger.core.domain.QiniuUploadDomain;

import java.util.List;

/**
 * @author alfred.yx
 * @version v 0.1 Oct 4, 2015 9:52:55 PM alfred Exp $
 */
public interface AttachService {
    /**
     * 根据附件id删除该附件
     *
     * @param attachId
     * @return Boolean
     */
    boolean deleteAttachById(Long attachId);

    /**
     * 判断抵押物ids是否都存在
     *
     * @param attachIds
     * @return
     */
    boolean isAllExist(List<Long> attachIds);

    /**
     * 判断抵押物id是否存在
     *
     * @param id
     * @return
     */
    boolean isExist(Long id);

    /**
     * 通过id获取附件模型
     *
     * @param id
     * @return
     */
    AttachDomain read(Long id);

    /**
     * 检查附件attachId是否为accountId所有
     *
     * @param attachId
     * @param accountId
     */
    boolean isOwner(Long attachId, Long accountId);

    /**
     * 检查附件attachIds是否都为accountId所有
     *
     * @param attachIds
     * @param accountId
     */
    boolean isOwnerOfAll(List<Long> attachIds, Long accountId);

    /**
     * 判断附件attachId是否为对应的attachtype
     *
     * @param attachId
     * @param attachType
     * @return
     */
    boolean isAttachType(Long attachId, AttachTypeEnum attachType);

    /**
     * 判断附件attachIds是否为对应的attachtype
     *
     * @param attachIds
     * @param attachType
     * @return
     */
    boolean isAllAttachType(List<Long> attachIds, AttachTypeEnum attachType);

    /**
     * 检查fileName是否已经存在
     *
     * @param fileName
     * @return
     */
    boolean isFilenameExist(String fileName);

    /**
     * 创建一个新的attach
     *
     * @param attachDomain
     * @return
     */
    AttachDomain create(AttachDomain attachDomain);

    /**
     * 查询附件信息
     *
     * @param query
     * @return
     */
    List<AttachDomain> query(AttachQuery query);

    /**
     * 根据附件id获取附件,授权url
     *
     * @param attachId
     * @return the qiniu attach by id
     */
    AttachDomain getAttachWithSignedUrlById(Long attachId);

    /**
     * 获取附件,授权url
     *
     * @param qiniuAttach
     * @return
     */
    AttachDomain getAttachWithSignedUrl(AttachDomain qiniuAttach);

    /**
     * 获取七牛云上传授权token
     *
     * @return the qiniu upload token
     */
    QiniuUploadDomain getQiniuUploadToken(AttachDomain attachDomain, Long accountId);

    /**
     * 七牛回调.
     *
     * @param attachDomain the attach domain
     * @return the attach domain
     */
    AttachDomain qiniuCallback(AttachDomain attachDomain, String originAuthorization, String callbackBody, String callbackContentType);

    /**
     * 根据attachIds获取附件列表,带有授权url
     *
     * @param attachIds
     * @return
     */
    List<AttachDomain> getAttachesWithSignedUrlByIds(List<Long> attachIds);

    /**
     * 获取附件列表,带有授权url
     *
     * @param qiniuAttaches
     * @return
     */
    List<AttachDomain> getAttachesWithSignedUrl(List<AttachDomain> qiniuAttaches);
}
