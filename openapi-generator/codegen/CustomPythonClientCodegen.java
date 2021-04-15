import org.openapitools.codegen.*;
import org.openapitools.codegen.languages.*;
import org.openapitools.codegen.utils.ProcessUtils;
import com.google.common.base.CaseFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class CustomPythonClientCodegen extends PythonClientCodegen {
    private static final Logger LOGGER = LoggerFactory.getLogger(PythonClientCodegen.class);

    @Override
    public void processOpts() {
        super.processOpts();

        String apiPackageDir = packagePath() + File.separatorChar + "api";

        File folder = new File(templateDir);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if(supportingFiles.stream().filter(f -> f.getTemplateFile().equals(file.getName())).findFirst().isPresent()) {
                    continue;
                }
                if(file.getName().equals("common_README.mustache")) {
                    continue;
                }
                LOGGER.info("adding custom template file {}", file.getName());
                if (file.getName().endsWith("_doc.mustache")) {
                    String outFilename = file.getName().substring(0, file.getName().indexOf("_doc.mustache")) + ".md";
                    supportingFiles.add(new SupportingFile(file.getName(), apiDocPath, outFilename));
                } else if (file.getName().endsWith("_api.mustache")) {
                    String outFilename = file.getName().substring(0, file.getName().indexOf(".mustache")) + ".py";
                    supportingFiles.add(new SupportingFile(file.getName(), apiPackageDir, outFilename));
                } else {
                    String outFilename = file.getName().substring(0, file.getName().indexOf(".mustache")) + ".py";
                    supportingFiles.add(new SupportingFile(file.getName(), packagePath(), outFilename));
                }
            }
        }
    }
}