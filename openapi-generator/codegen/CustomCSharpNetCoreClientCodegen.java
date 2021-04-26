import org.openapitools.codegen.*;
import org.openapitools.codegen.languages.*;
import org.openapitools.codegen.utils.ProcessUtils;
import com.google.common.base.CaseFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class CustomCSharpNetCoreClientCodegen extends CSharpNetCoreClientCodegen {
    private static final Logger LOGGER = LoggerFactory.getLogger(CSharpClientCodegen.class);

    @Override
    public void processOpts() {
        super.processOpts();

        String packageFolder = sourceFolder + File.separator + packageName;
        String apiPackageDir = packageFolder + File.separator + apiPackage;

        File folder = new File(templateDir);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if(supportingFiles.stream().filter(f -> f.getTemplateFile().equals(file.getName())).findFirst().isPresent()
                    || apiTemplateFiles.containsKey(file.getName()) || apiDocTemplateFiles.containsKey(file.getName()) {
                    continue;
                }
                LOGGER.info("adding custom template file {}", file.getName());
                if (file.getName().endsWith("_doc.mustache")) {
                    String outFilename = file.getName().substring(0, file.getName().indexOf("_doc.mustache")) + ".md";
                    supportingFiles.add(new SupportingFile(file.getName(), apiDocPath, outFilename));
                } else if (file.getName().endsWith("Api.mustache")) {
                    String outFilename = file.getName().substring(0, file.getName().indexOf(".mustache")) + ".cs";
                    supportingFiles.add(new SupportingFile(file.getName(), apiPackageDir, outFilename));
                } else {
                    String outFilename = file.getName().substring(0, file.getName().indexOf(".mustache")) + ".cs";
                    supportingFiles.add(new SupportingFile(file.getName(), packageFolder, outFilename));
                }
            }
        }
    }
}