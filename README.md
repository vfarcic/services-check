```bash
docker run --rm vfarcic/services-check
```

```bash
DNS=192.168.0.227
DOMAIN=http://int-services.mutuadepropietarios.es
docker run --rm \
  -v $PWD/application.conf:/app/application.conf \
  -e DOMAIN=$DOMAIN \
  --dns=$DNS \
  vfarcic/services-check
```

```bash
sbt "test-only -- html console"
```