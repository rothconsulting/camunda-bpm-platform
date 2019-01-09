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
package org.camunda.bpm.container.impl.jboss.service;

import org.camunda.bpm.container.impl.plugin.BpmPlatformPlugins;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;

/**
 * @author Thorben Lindhauer
 *
 */
public class MscBpmPlatformPlugins implements Service<BpmPlatformPlugins> {

  protected BpmPlatformPlugins plugins;

  public MscBpmPlatformPlugins(BpmPlatformPlugins plugins) {
    this.plugins = plugins;
  }

  @Override
  public BpmPlatformPlugins getValue() throws IllegalStateException, IllegalArgumentException {
    return plugins;
  }

  @Override
  public void start(StartContext context) throws StartException {
    // nothing to do
  }

  @Override
  public void stop(StopContext context) {
    // nothing to do
  }

}