/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
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
package org.camunda.bpm.engine.test.api.context;

import static org.assertj.core.api.Assertions.assertThat;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.context.DelegateExecutionContext;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.camunda.bpm.engine.test.util.ProcessEngineTestRule;
import org.camunda.bpm.engine.test.util.ProvidedProcessEngineRule;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;

/**
 * Represents test class to test the delegate execution context.
 *
 * @author Christopher Zell <christopher.zell@camunda.com>
 */
public class DelegateExecutionContextTest {

  protected static final BpmnModelInstance SERVICE_TASK_DELEGATE_PROCESS = Bpmn.createExecutableProcess("process1")
          .startEvent()
          .serviceTask("serviceTask1")
            .camundaClass(DelegateClass.class.getName())
          .endEvent()
          .done();


  protected static final BpmnModelInstance EXEUCTION_LISTENER_PROCESS = Bpmn.createExecutableProcess("process2")
          .startEvent()
            .camundaExecutionListenerClass(ExecutionListener.EVENTNAME_START, ExecutionListenerImpl.class.getName())
          .endEvent()
          .done();

  protected static final BpmnModelInstance SIGNAL_EVENT_PROCESS = Bpmn.createExecutableProcess("process3")
      .startEvent()
        .intermediateCatchEvent("catchSignal")
          .signal("${getCurrentActivityIdBean.getCurrentActivityId()}")
        .endEvent()
        .done();

  protected ProcessEngineRule engineRule = new ProvidedProcessEngineRule();
  protected ProcessEngineTestRule testHelper = new ProcessEngineTestRule(engineRule);

  @Rule
  public RuleChain ruleChain = RuleChain.outerRule(engineRule).around(testHelper);

  RuntimeService runtimeService;

  @Before
  public void setup() {
    runtimeService = engineRule.getRuntimeService();
  }

  @After
  public void tearDown() {
    Mocks.reset();
  }

  @Test
  public void testDelegateExecutionContext() {
    // given
    ProcessDefinition definition = testHelper.deployAndGetDefinition(SERVICE_TASK_DELEGATE_PROCESS);
    // a process instance with a service task and a java delegate
    runtimeService.startProcessInstanceById(definition.getId());

    //then delegation execution context is no more available
    DelegateExecution execution = DelegateExecutionContext.getCurrentDelegationExecution();
    assertThat(execution).isNull();
  }

  @Test
  public void testDelegateExecutionContextWithExecutionListener() {
    //given
    ProcessDefinition definition = testHelper.deployAndGetDefinition(EXEUCTION_LISTENER_PROCESS);
    // a process instance with a service task and an execution listener
    runtimeService.startProcessInstanceById(definition.getId());

    //then delegation execution context is no more available
    DelegateExecution execution = DelegateExecutionContext.getCurrentDelegationExecution();
    assertThat(execution).isNull();
  }

  @Test
  public void shouldFindSignalActivityIdThroughDelegateExecutionContext() {
    // given
    // register Bean
    Mocks.register("getCurrentActivityIdBean", new GetCurrentActivityIdBean());

    // a process instance with a signal event calling getCurrentActivityIdBean.getCurrentActivityId() to resolve referenced signal
    ProcessDefinition definition = testHelper.deployAndGetDefinition(SIGNAL_EVENT_PROCESS);
    runtimeService.startProcessInstanceById(definition.getId());

    // when send signal with current activity id as name
    runtimeService.createSignalEvent("catchSignal").send();

    // then signal was received and process instance finished
    assertThat(runtimeService.createProcessInstanceQuery().count()).as("process instance count").isZero();

  }

  public static class ExecutionListenerImpl implements ExecutionListener {

    @Override
    public void notify(DelegateExecution execution) throws Exception {
      //then delegation execution context is available
      DelegateExecution delegateExecution = DelegateExecutionContext.getCurrentDelegationExecution();
      assertThat(delegateExecution).isNotNull();
      assertThat(delegateExecution).isEqualTo(execution);
    }
  }

  public static class DelegateClass implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
      //then delegation execution context is available
      DelegateExecution delegateExecution = DelegateExecutionContext.getCurrentDelegationExecution();
      assertThat(delegateExecution).isNotNull();
      assertThat(delegateExecution).isEqualTo(execution);
    }
  }

  public class GetCurrentActivityIdBean {

    public String getCurrentActivityId() {
      return DelegateExecutionContext.getCurrentDelegationExecution().getCurrentActivityId();
    }
  }

}
