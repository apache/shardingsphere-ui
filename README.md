# ShardingSphere UI

[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

[![Build Status](https://builds.apache.org/job/shardingsphere-ui-dev/badge/icon)](https://builds.apache.org/job/shardingsphere-ui-dev/)
## Overview

ShardingSphere UI is a management background for [ShardingSphere](https://shardingsphere.apache.org/), including: dynamic configuration, Data governance, etc.

### ShardingSphere UI Frontend

shardingsphere-ui-frontend based on [Vue.js](https://github.com/vuejs/vue) and use the UI Toolkit [element](https://github.com/ElemeFE/element).

* [shardingsphere-ui-frontend/README.md](shardingsphere-ui-frontend/README.md)

### ShardingSphere UI Backend

shardingsphere-ui-backend is a standard spring boot project.

## How to Build

```bash
git clone https://github.com/apache/shardingsphere-ui.git
cd shardingsphere-ui/
mvn clean package -Prelease
```

Get the package in `shardingsphere-ui/shardingsphere-ui-distribution/shardingsphere-ui-bin-distribution/target/apache-shardingsphere-${latest.release.version}-shardingsphere-ui-bin.tar.gz`

## Docker Image

* [Docker hub](https://hub.docker.com/r/apache/shardingsphere-ui)
* Get latest shardingsphere-ui docker image
```bash
$ docker pull apache/shardingsphere-ui
```
