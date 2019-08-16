package org.apache.drill.exec.store.cube;

import org.apache.drill.common.config.LogicalPlanPersistence;
import org.apache.drill.common.exceptions.ExecutionSetupException;
import org.apache.drill.common.scanner.persistence.ScanResult;
import org.apache.drill.exec.store.SchemaConfig;
import org.apache.drill.exec.store.dfs.*;

import java.io.IOException;
import java.util.List;

public class UserWorkspaceSchemaFactory extends WorkspaceSchemaFactory {

	private final String schemaName;

	public UserWorkspaceSchemaFactory(FileSystemPlugin plugin, String schemaName, String storageEngineName, WorkspaceConfig config, List<FormatMatcher> formatMatchers, LogicalPlanPersistence logicalPlanPersistence, ScanResult scanResult) throws ExecutionSetupException {
		super(plugin, schemaName, storageEngineName, config, formatMatchers, logicalPlanPersistence, scanResult);
		this.schemaName = schemaName;
	}

	@Override
	public WorkspaceSchema createSchema(List<String> parentSchemaPath, SchemaConfig schemaConfig, DrillFileSystem fs) throws IOException {
		return new WorkspaceSchemaFactory.WorkspaceSchema(parentSchemaPath, schemaName, schemaConfig, fs);
	}
}
