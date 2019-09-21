package cn.edu.bupt.dao.Cassandra;

import com.datastax.driver.core.*;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class CassandraCluster {

    @Value("${cassandra.cluster_name}")
    private String clusterName;
    @Value("${cassandra.contactpoints}")
    private String contactpoints;
    @Value("${cassandra.port}")
    private int port;
    @Value("${cassandra.keyspace_name}")
    private String keyspaceName;
    @Value("${cassandra.compression}")
    private String compression;
    @Value("${cassandra.ssl}")
    private Boolean ssl;
    @Value("${cassandra.jmx}")
    private Boolean jmx;
    @Value("${cassandra.metrics}")
    private Boolean metrics;
    @Value("${cassandra.credentials}")
    private Boolean credentials;
    @Value("${cassandra.username}")
    private String username;
    @Value("${cassandra.password}")
    private String password;
    @Value("${cassandra.init_timeout_ms}")
    private long initTimeout;
    @Value("${cassandra.init_retry_interval_ms}")
    private long initRetryInterval;

    @Autowired
    private CassandraSocketOptions socketOpts;

    @Autowired
    private CassandraQueryOptions queryOpts;

    private Cluster cluster;

    @Getter(AccessLevel.NONE) private Session session;

    private MappingManager mappingManager;

    public <T> Mapper<T> getMapper(Class<T> clazz) {
        return mappingManager.mapper(clazz);
    }


    @PostConstruct
    protected void init() {
        Cluster.Builder builder = Cluster.builder()
                .addContactPoints(contactpoints).withPort(port)
                .withClusterName(clusterName)
                .withSocketOptions(socketOpts.getOpts())
                .withPoolingOptions(new PoolingOptions()
                        .setMaxRequestsPerConnection(HostDistance.LOCAL, 32768)
                        .setMaxRequestsPerConnection(HostDistance.REMOTE, 32768));
        builder.withQueryOptions(queryOpts.getOpts());
        builder.withCompression(StringUtils.isEmpty(compression) ? ProtocolOptions.Compression.NONE : ProtocolOptions.Compression.valueOf(compression.toUpperCase()));
        if (ssl) {
            builder.withSSL();
        }
        if (!jmx) {
            builder.withoutJMXReporting();
        }
        if (!metrics) {
            builder.withoutMetrics();
        }
        if (credentials) {
            builder.withCredentials(username, password);
        }
        cluster = builder.build();
        cluster.init();
        initSession();
    }

    private void initSession() {
        long endTime = System.currentTimeMillis() + initTimeout;
        while (System.currentTimeMillis() < endTime) {
            try {

                if (this.keyspaceName != null) {
                    session = cluster.connect(keyspaceName);
                } else {
                    session = cluster.connect();
                }
                mappingManager = new MappingManager(session);
                break;
            } catch (Exception e) {
                try {
                    Thread.sleep(initRetryInterval);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public Session getSession() {
        if (session == null) {
            initSession();
        }
        return session;
    }

    public String getKeyspaceName() {
        return keyspaceName;
    }

    @PreDestroy
    public void close() {
        if (cluster != null) {
            cluster.close();
        }
    }

    public ConsistencyLevel getDefaultReadConsistencyLevel() {
        return queryOpts.getDefaultReadConsistencyLevel();
    }

    public ConsistencyLevel getDefaultWriteConsistencyLevel() {
        return queryOpts.getDefaultWriteConsistencyLevel();
    }

}
