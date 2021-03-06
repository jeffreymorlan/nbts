/*
 * Copyright 2015 Everlaw
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package netbeanstypescript;

import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.netbeans.modules.csl.api.ColoringAttributes;
import org.netbeans.modules.csl.api.OccurrencesFinder;
import org.netbeans.modules.csl.api.OffsetRange;
import org.netbeans.modules.parsing.spi.Parser;
import org.netbeans.modules.parsing.spi.Scheduler;
import org.netbeans.modules.parsing.spi.SchedulerEvent;

/**
 *
 * @author jeffrey
 */
public class TSOccurrencesFinder extends OccurrencesFinder<Parser.Result> {

    private int caretPosition;
    private Map<OffsetRange, ColoringAttributes> result;

    @Override
    public void setCaretPosition(int pos) {
        caretPosition = pos;
    }

    @Override
    public Map<OffsetRange, ColoringAttributes> getOccurrences() {
        return result;
    }

    @Override
    public void run(Parser.Result t, SchedulerEvent se) {
        Object occurrences = TSService.call("getOccurrencesAtPosition",
                t.getSnapshot().getSource().getFileObject(), caretPosition);
        Map<OffsetRange, ColoringAttributes> ranges = new HashMap<>();
        if (occurrences != null) {
            for (Object o: (JSONArray) occurrences) {
                JSONObject occ = (JSONObject) o;
                int start = ((Number) occ.get("start")).intValue();
                int end = ((Number) occ.get("end")).intValue();
                ranges.put(new OffsetRange(start, end), ColoringAttributes.MARK_OCCURRENCES);
            }
        }
        result = ranges;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public Class<? extends Scheduler> getSchedulerClass() {
        return Scheduler.CURSOR_SENSITIVE_TASK_SCHEDULER;
    }

    @Override
    public void cancel() {}
}
