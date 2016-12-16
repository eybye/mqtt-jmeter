/*
 * Copyright 2016 Hemika Yasinda Kodikara
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jmeter.protocol.mqtt.utilities;

import org.apache.commons.lang3.StringUtils;
import org.apache.jorphan.io.TextFile;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.log.Logger;

/**
 * Utility class for plugin
 */
public class Utils {

    /**
     * Creates a UUID. The UUID is modified to avoid "ClientId longer than 23 characters" for MQTT.
     *
     * @return A UUID as a string.
     */
    public static String UUIDGenerator() {
        String clientId = System.currentTimeMillis() + "." + System.getProperty("user.name");
        clientId = StringUtils.substring(clientId, 0, 23);
        return clientId;
    }

    /**
     * The implementation uses TextFile to load the contents of the file and
     * returns a string.
     *
     * @param path path to the file to read in
     * @return the contents of the file
     */
    public static String getFileContent(String path) {
        TextFile tf = new TextFile(path);
        return tf.getText();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static String replaceVariables(String inputString, Logger log) {
        JMeterVariables jmvari = JMeterContextService.getContext()
            .getVariables();

        String outString = StringUtils.EMPTY;

        int idxStart = inputString.indexOf("${");
        int idxStop = 0;
        while (idxStart != -1) {
            outString = outString.concat(inputString.substring(idxStart));
            idxStart += 2;
            idxStop = inputString.indexOf("}", idxStart);
            String varName = inputString.substring(idxStart, idxStop);
            log.info("Variable: " + varName);
            idxStop += 1;
            String var = jmvari.get(varName);
            log.info("Variable content: " + var);
            outString = outString.concat(var);

            idxStart = inputString.indexOf("${", idxStop);
        }
        outString = outString.concat(inputString.substring(idxStop, inputString.length()));
        log.info("Final string: " + outString);
        return outString;
    }
}
