import org.openapitools.codegen.*;
import org.openapitools.codegen.languages.*;
import org.openapitools.codegen.utils.ProcessUtils;
import com.google.common.base.CaseFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class CustomJavaClientCodegen extends JavaClientCodegen {
    private static final Logger LOGGER = LoggerFactory.getLogger(JavaClientCodegen.class);
    
    @Override
    public void processOpts() {
        super.processOpts();

        final String apiFolder = (sourceFolder + '/' + apiPackage).replace(".", "/");
        final String invokerFolder = (sourceFolder + '/' + invokerPackage).replace(".", "/");
        
        File folder = new File(templateDir);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if(supportingFiles.stream().filter(f -> f.getTemplateFile().equals(file.getName())).findFirst().isPresent()
                    || apiTemplateFiles.containsKey(file.getName())) {
                    continue;
                }
                LOGGER.info("adding custom template file {}", file.getName());
                if (file.getName().endsWith("_doc.mustache")) {
                    String outFilename = file.getName().substring(0, file.getName().indexOf("_doc.mustache")) + ".md";
                    supportingFiles.add(new SupportingFile(file.getName(), apiDocPath, outFilename));
                } else if (file.getName().endsWith("Api.mustache")) {
                    String outFilename = file.getName().substring(0, file.getName().indexOf(".mustache")) + ".java";
                    supportingFiles.add(new SupportingFile(file.getName(), apiFolder, outFilename));
                } else {
                    String outFilename = file.getName().substring(0, file.getName().indexOf(".mustache")) + ".java";
                    supportingFiles.add(new SupportingFile(file.getName(), invokerFolder, outFilename));
                }
            }
        }
    }
}