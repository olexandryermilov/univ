package application;

import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.Parameters;

@Component
public class GraphQLDataFetchers {

    @Autowired
    DatabaseService databaseService;

    public DataFetcher getDatabaseByNameFetcher(){
        return environment -> {
            String databaseName = environment.getArgument(Parameters.databaseName());
            return databaseService.getDatabaseService(databaseName);
        };
    }
}
