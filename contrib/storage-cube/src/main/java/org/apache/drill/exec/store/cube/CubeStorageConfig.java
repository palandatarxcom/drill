/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.drill.exec.store.cube;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.collect.ImmutableMap;
import org.apache.drill.common.logical.FormatPluginConfig;
import org.apache.drill.exec.store.dfs.FileSystemConfig;
import org.apache.drill.exec.store.dfs.WorkspaceConfig;
import org.apache.drill.exec.store.parquet.ParquetFormatConfig;

import java.util.Map;

@JsonTypeName(CubeStorageConfig.NAME)
public class CubeStorageConfig extends FileSystemConfig {

  public static final String NAME = "cube";

  public static final ParquetFormatConfig FORMAT_CONFIG = new ParquetFormatConfig();

  public static final WorkspaceConfig DEFAULT = new WorkspaceConfig("/cube", true, "parquet", false);

  public CubeStorageConfig(String connection, Map<String, String> config) {
    super(connection, config, ImmutableMap.of("cube", DEFAULT), ImmutableMap.of("parquet", FORMAT_CONFIG));
  }
}
