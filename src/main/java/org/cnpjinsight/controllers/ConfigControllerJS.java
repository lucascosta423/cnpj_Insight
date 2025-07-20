package org.cnpjinsight.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ConfigControllerJS {

    @Value("${app.backend.url.export}")
    private String backendUrlExport;

    @Value("${app.backend.url}")
    private String backendUrlApi;

    @GetMapping(value = "/config.js", produces = "application/javascript")
    @ResponseBody
    public String configJs() {
        return "const appConfig = { " +
                "backendUrlExport: '" + backendUrlExport + "' " +
                ", backendUrlApi: '" + backendUrlApi + "' " +
                "};";
    }
}

