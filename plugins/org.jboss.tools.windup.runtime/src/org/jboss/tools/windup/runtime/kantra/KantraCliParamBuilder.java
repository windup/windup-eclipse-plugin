
/*---------------------------------------------------------------------------------------------
 *  Copyright (c) Red Hat. All rights reserved.
 *--------------------------------------------------------------------------------------------*/
package org.jboss.tools.windup.runtime.kantra;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;


public class KantraCliParamBuilder {

    public static List<String> buildParams(List<String> input, String output, List<String> target) {
        List<String> params = Lists.newArrayList();
        params.add("analyze");

        // input
        params.add("--input");
//        List<String> input = (List<String>)options.get("input");
        input.forEach(path -> params.add(path));

        // output
        params.add("--output");
//        String output = (String)options.get("output");
        params.add(output);

        // target
//        List<String> target = (List<String>)options.get("target");
        if (target == null || target.isEmpty()) {
            target = Lists.newArrayList();
            target.add("eap7");
        }
        params.add("--target");

        for (String aTarget : target) {
            params.add(aTarget);
        }
//        params.add(String.join(",", target));

        return params;
    }
}
