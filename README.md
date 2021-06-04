# Library Template

[![Build](https://github.com/michaelruocco/burger-promo/workflows/pipeline/badge.svg)](https://github.com/michaelruocco/burger-promo/actions)
[![codecov](https://codecov.io/gh/michaelruocco/burger-promo/branch/master/graph/badge.svg?token=FWDNP534O7)](https://codecov.io/gh/michaelruocco/burger-promo)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/272889cf707b4dcb90bf451392530794)](https://www.codacy.com/gh/michaelruocco/burger-promo/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=michaelruocco/burger-promo&amp;utm_campaign=Badge_Grade)
[![BCH compliance](https://bettercodehub.com/edge/badge/michaelruocco/burger-promo?branch=master)](https://bettercodehub.com/)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=michaelruocco_burger-promo&metric=alert_status)](https://sonarcloud.io/dashboard?id=michaelruocco_burger-promo)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=michaelruocco_burger-promo&metric=sqale_index)](https://sonarcloud.io/dashboard?id=michaelruocco_burger-promo)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=michaelruocco_burger-promo&metric=coverage)](https://sonarcloud.io/dashboard?id=michaelruocco_burger-promo)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=michaelruocco_burger-promo&metric=ncloc)](https://sonarcloud.io/dashboard?id=michaelruocco_burger-promo)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.michaelruocco/burger-promo.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.michaelruocco%22%20AND%20a:%22burger-promo%22)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## Overview

This repo contains a toy implementation for a backend promo API where each promo can be used exactly
once per account and has a max number of claims available.

## Useful Commands

```gradle
// cleans build directories
// prints currentVersion
// formats code
// builds code
// runs tests
// checks for gradle issues
// checks dependency versions
./gradlew clean currentVersion dependencyUpdates lintGradle spotlessApply build
```
```jmeter
mkdir -p build/reports/jmeter/html;
rm -rf build/reports/jmeter/log/*;
jmeter --nongui \
       --forceDeleteResultFile \
       --testfile src/performance-test/free-burger-promo.jmx \
       --logfile build/reports/jmeter/log/free-burger-promo.jtl \
       --reportatendofloadtests \
       --reportoutputfolder build/reports/jmeter/html
```