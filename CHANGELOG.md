# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Changed

- fj-universe-tool parent version set to 0.5.8

## [0.2.0] - 2023-11-07

### Added

- parameter 'output-xslt' (use 'legacy' value for previous behavior, can lead to different output based on [jdk](https://bugs.openjdk.org/browse/JDK-8262285?attachmentViewMode=list))

### Changed

- fj-universe-tool parent version set to 0.5.5
- use a custom [xlst](src/main/resources/tool-i18n-xslt/default-print-xml.xslt) by default for xml output handling

### Fixed

- different output based on [jdk](https://bugs.openjdk.org/browse/JDK-8262285?attachmentViewMode=list)

## [0.1.4] - 2023-10-09

### Added

- 'regex' mode for 'normalize' and 'node' text handlers.

### Changed

- fj-universe-tool parent version set to 0.5.2

## [0.1.3] - 2023-09-28

### Added

- issueManagement

### Changed

- fj-universe-tool parent set to 0.4.7

### Fixed

- placeholder in LICENSE set
- software version badge links

## [0.1.2] - 2023-09-28

### Added

- Maven dependency section in README.md
- Help command

### Changed

- fj-universe-tool parent set to 0.4.6
- parameter help in readme
- parameter convert-config now accet path in class loader by using cl:// prefix

### Fixed

- java runtime version in README.md set to 11
- sample convert-config.xml documentation
- Typo in README.md
- Typo in CHANGELOG.md

## [0.1.1] - 2023-09-26

### Added

- Text handler to cut maximum length
- altValue and info attributes for text handlers configuration

## Changed

- maven-shade-plugin set to 3.5.1
- maven-jar-plugin set to 3.3.0

### Fixed

- folder creation on directory recurse (#8)

## [0.1.0] - 2023-09-26

### Added

- Tool and API mode
- bundled rules for LabelExtract and ElementRemove
- badges and documentation
- sonar cloud and build workflow
