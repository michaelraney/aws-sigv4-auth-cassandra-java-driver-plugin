package software.aws.mcs.auth;

/*-
 * #%L
 * AWS SigV4 Auth Java Driver 4.x Plugin
 * %%
 * Copyright (C) 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
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
 * #L%
 */



import com.datastax.driver.core.*;
import com.datastax.driver.core.policies.DCAwareRoundRobinPolicy;
import com.datastax.driver.core.policies.TokenAwarePolicy;
import sun.security.ssl.SSLContextImpl;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import javax.net.ssl.SSLContext;

public class TestSigV4 {
    static String DEFAULT_CONTACT_POINT = "cassandra.us-east-1.amazonaws.com";

    public static void main(String[] args) throws Exception {

        System.out.println("Using endpoints: " + DEFAULT_CONTACT_POINT);

        // The CqlSession object is the main entry point of the driver.
        // It holds the known state of the actual Cassandra cluster (notably the Metadata).
        // This class is thread-safe, you should create a single instance (per target Cassandra cluster), and share
        // it throughout your application.

       // final SSLContext sslContext = SSLContext.getInstance("TLS");
        //sslContext.init(null, trustManagers, null);

        //final JdkSSLOptions sslOptions = JdkSSLOptions.builder()
        //        .withSSLContext(sslContext)
        //        .build();

       // System.setProperty("javax.net.ssl.trustStore", "/Users/mjraney/.cassandra/cassandra_truststore.jks");
       // System.setProperty("javax.net.ssl.trustStorePassword", "amazon");

        Session session = Cluster.builder()
                .addContactPoint(DEFAULT_CONTACT_POINT)
                .withPort(9142)
             //.addContactPointsWithPorts(contactPoints)
             .withAuthProvider(new SigV4AuthProvider("us-east-1"))

              //  .withAuthProvider(new PlainTextAuthProvider("alice-at-963740746376" , "fLyWYFlTCD5J2gzGAZ+dzxUw8QaJvSFx50SF9oGidkI="))
             .withSSL()
             //.withLoadBalancingPolicy((new TokenAwarePolicy(DCAwareRoundRobinPolicy.builder().withLocalDc("us-east-1").build())))
             .build().connect();




            // We use execute to send a query to Cassandra. This returns a ResultSet, which is essentially a collection
            // of Row objects.
            ResultSet rs = session.execute("select release_version from system.local");
            //  Extract the first row (which is the only one in this case).
            Row row = rs.one();

            // Extract the value of the first (and only) column from the row.
            String releaseVersion = row.getString("release_version");
            System.out.printf("Cassandra version is: %s%n", releaseVersion);

    }
}
