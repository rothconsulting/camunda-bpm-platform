/*
 * Copyright © 2013-2018 camunda services GmbH and various authors (info@camunda.com)
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
package org.camunda.bpm.engine.test.util;

import java.util.ArrayList;
import java.util.List;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;

/**
 * @author Daniel Meyer
 *
 */
public class TestExecutionListener implements ExecutionListener {

  public static List<String> collectedEvents = new ArrayList<String>();

  public void notify(DelegateExecution execution) throws Exception {
    String counterKey = execution.getCurrentActivityId() + "-" +execution.getEventName();
    collectedEvents.add(counterKey);
  }
  
  public static void reset() {
    collectedEvents.clear();
  }
  
}