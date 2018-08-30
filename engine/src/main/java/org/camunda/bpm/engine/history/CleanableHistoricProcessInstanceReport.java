/* Licensed under the Apache License, Version 2.0 (the "License");
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

package org.camunda.bpm.engine.history;

import org.camunda.bpm.engine.exception.NotValidException;
import org.camunda.bpm.engine.query.Query;

/**
 * Defines a report query for cleanable process instances.
 *
 */
public interface CleanableHistoricProcessInstanceReport extends Query<CleanableHistoricProcessInstanceReport, CleanableHistoricProcessInstanceReportResult> {

  /**
   * Only takes historic process instances into account for the given process definition ids.
   *
   * @throws NotValidException if one of the given ids is null
   */
  CleanableHistoricProcessInstanceReport processDefinitionIdIn(String... processDefinitionIds);

  /**
   * Only takes historic process instances into account for the given process definition keys.
   *
   * @throws NotValidException if one of the given keys is null
   */
  CleanableHistoricProcessInstanceReport processDefinitionKeyIn(String... processDefinitionKeys);

  /**
   * Only select historic process instances with one of the given tenant ids.
   *
   * @throws NotValidException if one of the given ids is null
   */
  CleanableHistoricProcessInstanceReport tenantIdIn(String... tenantIds);

  /**
   * Only selects historic process instances which have no tenant id.
   */
  CleanableHistoricProcessInstanceReport withoutTenantId();

  /**
   * Only selects historic process instances which have more than zero finished instances.
   */
  CleanableHistoricProcessInstanceReport compact();

  /**
   * Order by finished process instances amount (needs to be followed by {@link #asc()} or {@link #desc()}).
   */
  CleanableHistoricProcessInstanceReport orderByFinished();

  /**
   * Selects historic process instances by using hierarchical attributes (removal time).
   */
  CleanableHistoricProcessInstanceReport usesHierarchicalHistoryCleanup();

}