package com.janardan.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created for zeal on 3/11/20
 */
@Component
public class PrepareHtmlForPdfService {

    private TemplateEngine templateEngine;

    @Autowired
    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    /**
     * prepare thymeleaf context for invoice
     *
     * @return String of pdf formatted data
     * @param fileName
     */
    public String demo(String fileName) {
        Context context = new Context();
        context.setVariable("name", "Janardan chaudhary");
        String process = templateEngine.process(fileName, context);
        Map<String, String> values = new HashMap<>();
        values.put("$Finance$", "Finance value");
        values.put("$Name$", "Janardan chaudhary");
        for (Map.Entry<String, String> entry : values.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            process = process.replace(key, value);
        }
        return process;
    }

}
