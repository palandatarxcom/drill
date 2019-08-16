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

import org.apache.drill.categories.CubeStorageTest;
import org.apache.drill.exec.server.DrillbitContext;
import org.apache.drill.exec.store.StoragePluginRegistryImpl;
import org.apache.drill.test.ClusterFixture;
import org.apache.drill.test.ClusterTest;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * JDBC storage plugin tests against H2.
 */
@Category(CubeStorageTest.class)
public class TestCubePlugin extends ClusterTest {

    @BeforeClass
    public static void setUp() throws Exception {

        startCluster(ClusterFixture.builder(dirTestWatcher));

        CubeStorageConfig config = new CubeStorageConfig(
                "file:///",
                null
        );

        config.setEnabled(true);
        String pluginName = "cube";
        DrillbitContext context = cluster.drillbit().getContext();
        CubeStoragePlugin plugin = new CubeStoragePlugin(config, context, pluginName);
        StoragePluginRegistryImpl pluginRegistry = (StoragePluginRegistryImpl) context.getStorage();
        pluginRegistry.addPluginToPersistentStoreIfAbsent(pluginName, config, plugin);

    }


    @Test
    public void schemas() throws Exception {
        queryBuilder().sql("show schemas").printCsv();
        queryBuilder().sql("create table cube.user.`sample` as select 'sample' as `sample` from (values(1))").printCsv();
        queryBuilder().sql("select * from cube.user.`sample`").printCsv();
        queryBuilder().sql("use cube.user").run().succeeded();
        queryBuilder().sql("select * from `sample`").printCsv();
        queryBuilder().sql("drop table if exists cube.user.`sample`").printCsv();

    }

}
