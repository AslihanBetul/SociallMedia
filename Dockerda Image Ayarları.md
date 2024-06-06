# DOCKER KURULUMLAR

## Docker üzerinde postgreSQL Kurulumu:

```bash
docker run --name java14-postgres -d -e POSTGRES_USER=java14user -e POSTGRES_PASSWORD=root -p 5433:5432 postgres
```

# Docker üzerinde MongoDb kurulumu ve Çalıştırma:
```bash
docker run --name java14-mongoDB -d -e MONGO_INITDB_ROOT_USERNAME=java14user -e MONGO_INITDB_ROOT_PASSWORD=root -p 27027:27017 mongo:jammy 
```
db oluşturduktan sonra user atamak için aşağıdaki komutu mongosh'a yazalım.
use db_adı
db.createUser({user:"bilge",pwd:"bilge!123",roles: ["readWrite","dbAdmin"] } )

## Dockerda Zipkin Container ayağa kaldırma
```bash
docker run --name java14-zipkin -d -p 9411:9411 openzipkin/zipkin
```

## Dockerda RabbitMQ kurulumu:
* default port: amqp: 5672 (arka planda microservislerin iletişimi için)
* management port : http: 15672 (Yönetim işlemleri için yayın yaptığı port)
```bash
docker run --hostname java14-rabbit --name java14-rabbit -d -e RABBITMQ_DEFAULT_USER=java14user -e RABBITMQ_DEFAULT_PASS=root -p 15672:15672 -p 5672:5672 --memory=128m rabbitmq:3-management
```

## Dockerda Redis kurulumu:
```bash
docker run --name java14-redis -d -p 6379:6379 --memory=256m redis
```
## Dockerda RedisInsight kurulumu:
```bash
docker run --name java14-redisinsight -d -p 8001:8001 redislabs/redisinsight:1.14.0
```

## Dockerda Elasticsearch kurulumu:

## Image Çekme:
```bash
docker pull elasticsearch:8.11.1
```

## Kibana Image Çekme
```bash
docker pull kibana:8.11.1
```

## Elastic ile Kibananın Docker ortamında haberleşmesi için Network oluşturma:
```bash
docker network create elasticnetwork
```

## ElasticSearch docker image ayağa kaldırma:
```bash
docker run -d --name java14-elasticsearch --net elasticnetwork -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" -e "xpack.security.enabled=false" -e "xpack.security.http.ssl.enabled=false"  -m 1GB elasticsearch:8.11.1
```

## Kibana docker image ayağa kaldırma:

```bash
docker run -d --name java14-kibana --net elasticnetwork -p 5601:5601 kibana:8.11.1
```