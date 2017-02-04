package deskcomm_restapi.support;

import org.apache.commons.io.FilenameUtils;

/**
 * Created by Jay Rathod on 03-02-2017.
 */
public class FileUtils {
    public static boolean isImageFile(String fileName) {
        String fileExtension = FilenameUtils.getExtension(fileName);
        return fileExtension.equalsIgnoreCase("jpg") ||
                fileExtension.equalsIgnoreCase("jpeg") ||
                fileExtension.equalsIgnoreCase("png") ||
                fileExtension.equalsIgnoreCase("bmp");
    }

}
