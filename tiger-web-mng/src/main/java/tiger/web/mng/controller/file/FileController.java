/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.mng.controller.file;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tiger.biz.mng.attach.support.AttachManager;
import tiger.common.util.FileUtil;
import tiger.core.domain.AttachDomain;

/**
 * @author alfred_yuan
 * @version v 0.1 21:30 alfred_yuan Exp $
 */
@Controller
@RequiresAuthentication
public class FileController {
    @Autowired
    private AttachManager attachManager;

    @RequestMapping(value = "/file", method = RequestMethod.POST)
    @ResponseBody
    public AttachDomain uploadImageFile(MultipartFile file) {
        return attachManager.uploadPublicAttach(file);
    }

    @RequestMapping(value = "/file/preview/{id}", method = RequestMethod.GET)
    public String previewAttach(@PathVariable("id") Long id,
                                @ModelAttribute("model")ModelMap model) {
        AttachDomain attach = attachManager.get(id);
        model.addAttribute("attach", attach);

        String fileType;
        if (FileUtil.isImageFileFromName(attach.getName())) {
            fileType = "IMAGE";
        } else {
            fileType = "UNKNOWN";
        }
        model.addAttribute("fileType", fileType);

        return "base/modal/filePreview";
    }

}
