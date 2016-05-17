/*
 * Copyright 2016 Javier.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jutils.jhardware.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author Javier
 */
public enum DirectXInfoLoader {
    get;

    private boolean loaded = false;
    private Document directXData;

    private DirectXInfoLoader() {
        if (!this.loaded) {
            //TODO: handle return code
            this.load();
            this.loaded = true;
        }
    }

    private void load() {
        Path tempFile;
        try {
            tempFile = Files.createTempFile("dxdata_" + System.currentTimeMillis(), ".tmp");
            tempFile.toFile().deleteOnExit();

            HardwareInfoUtils.executeCommand("dxdiag", "/x", tempFile.toAbsolutePath().toString());
        } catch (IOException ex) {
            Logger.getLogger(DirectXInfoLoader.class.getName()).log(Level.SEVERE, "Error creating directX data", ex);
            return;
        }

        DocumentBuilderFactory dbFactory
                = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            this.directXData = dBuilder.parse(tempFile.toFile());
            this.directXData.getDocumentElement().normalize();
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(DirectXInfoLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Map<String, String> getDisplayInfo() {
        return null;
    }
}