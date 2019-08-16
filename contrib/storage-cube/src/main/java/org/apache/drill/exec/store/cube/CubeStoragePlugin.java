package org.apache.drill.exec.store.cube;

import com.google.common.collect.Lists;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.drill.common.exceptions.ExecutionSetupException;
import org.apache.drill.common.logical.FormatPluginConfig;
import org.apache.drill.exec.server.DrillbitContext;
import org.apache.drill.exec.store.AbstractSchema;
import org.apache.drill.exec.store.SchemaConfig;
import org.apache.drill.exec.store.dfs.*;
import org.apache.drill.exec.util.ImpersonationUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CubeStoragePlugin extends FileSystemPlugin {

	private final List<FormatMatcher> formatMatchers;

	public CubeStoragePlugin(CubeStorageConfig config, DrillbitContext context, String name) throws ExecutionSetupException {
		super(config, context, name);

		formatMatchers = new ArrayList<>();

		for (FormatPluginConfig formatName : config.getFormats().values()) {

			FormatPlugin formatPlugin = this.getFormatPlugin(formatName);
			if (formatPlugin != null) {
				formatMatchers.add(formatPlugin.getMatcher());
			}
		}
	}

	@Override
	public void registerSchemas(SchemaConfig schemaConfig, SchemaPlus parent) throws IOException {
		CubeSchema schema = new CubeSchema(this, schemaConfig);
		parent.add(schema.getName(), schema);
	}

	public class CubeSchema extends AbstractSchema {

		private final CubeStoragePlugin plugin;

		private final SchemaConfig schemaConfig;

		public CubeSchema(CubeStoragePlugin plugin, SchemaConfig schemaConfig) {
			super(Collections.emptyList(), plugin.getName());
			this.plugin = plugin;
			this.schemaConfig = schemaConfig;
		}

		@Override
		public String getTypeName() {
			return CubeStorageConfig.NAME;
		}


		@Override
		public Schema getSubSchema(String name) {
			DrillFileSystem fs = ImpersonationUtil.createFileSystem(schemaConfig.getUserName(), plugin.getFsConf());

			WorkspaceConfig workspaceConfig = new WorkspaceConfig(CubeStorageConfig.DEFAULT.getLocation() + File.separator + name, true, "parquet", false);

			try {
				UserWorkspaceSchemaFactory workspaceSchemaFactory = new UserWorkspaceSchemaFactory(
					plugin,
					name,
					plugin.getName(),
					workspaceConfig,
					formatMatchers,
					context.getLpPersistence(),
					context.getClasspathScan()
				);

				return workspaceSchemaFactory.createSchema(Collections.emptyList(), schemaConfig, fs);

			} catch (ExecutionSetupException | IOException e) {
				e.printStackTrace();
			}

			return null;
		}
	}




}
