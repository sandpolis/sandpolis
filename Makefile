## This makefile provides convenient access to optional utilities. It doesn't
## actually build anything.

updateSubmodules:
	git submodule foreach git pull origin master

createVersionCommit:
	git commit -m "v$(shell date +'%Y.%m.%d')"
	git tag "v$(shell date +'%Y.%m.%d')"

updateJavaVersion:
	[ "${VERSION}" != "" ]

	find . -type f -name 'Dockerfile' -exec \
		sed -i "s/FROM adoptopenjdk:..-/FROM adoptopenjdk:${VERSION}-/g" {} \;

	find . -type f -name '*.yml' -exec \
		sed -i "s/java-version: ../java-version: ${VERSION}/g" {} \;

	find . -type f -name '*.gradle.kts' -exec \
		sed -i "s/JavaLanguageVersion.of(..)/JavaLanguageVersion.of(${VERSION})/g;s/jvmTarget = \"..\"/jvmTarget = \"${VERSION}\"/g;s/version = \"..\"/version = \"${VERSION}\"/g" {} \;

enableAgentMicro: enableModules
	git submodule update --init com.sandpolis.agent.micro
	(cd com.sandpolis.agent.micro; git checkout master; git pull origin master)

disableAgentMicro:
	git submodule deinit --force com.sandpolis.agent.micro

enableAgentNano: enableModules
	git submodule update --init com.sandpolis.agent.nano
	(cd com.sandpolis.agent.nano; git checkout master; git pull origin master)

disableAgentNano:
	git submodule deinit --force com.sandpolis.agent.nano

enableAgentVanilla: enableModules
	git submodule update --init com.sandpolis.agent.vanilla
	(cd com.sandpolis.agent.vanilla; git checkout master; git pull origin master)

disableAgentVanilla:
	git submodule deinit --force com.sandpolis.agent.vanilla

enableClientAscetic: enableModules
	git submodule update --init com.sandpolis.client.ascetic
	(cd com.sandpolis.client.ascetic; git checkout master; git pull origin master)

disableClientAscetic:
	git submodule deinit --force com.sandpolis.client.ascetic

enableClientLifegem: enableModules
	git submodule update --init com.sandpolis.client.lifegem
	(cd com.sandpolis.client.lifegem; git checkout master; git pull origin master)

disableClientLifegem:
	git submodule deinit --force com.sandpolis.client.lifegem

enableClientLockstone: enableModules
	git submodule update --init com.sandpolis.client.lockstone
	(cd com.sandpolis.client.lockstone; git checkout master; git pull origin master)

disableClientLockstone:
	git submodule deinit --force com.sandpolis.client.lockstone

enableDistagent:
	git submodule update --init com.sandpolis.distagent
	(cd com.sandpolis.distagent; git checkout master; git pull origin master)

disableDistagent:
	git submodule deinit --force com.sandpolis.distagent

enableServerVanilla: enableModules
	git submodule update --init com.sandpolis.server.vanilla
	(cd com.sandpolis.server.vanilla; git checkout master; git pull origin master)

disableServerVanilla:
	git submodule deinit --force com.sandpolis.server.vanilla

enableModules:
	git submodule update --init module
	(cd module; git submodule foreach 'git checkout master && git pull origin master')

disableModules:
	git submodule deinit --force module

enablePlugins:
	git submodule update --init plugin
	(cd plugin; git submodule foreach 'git checkout master && git pull origin master')

disablePlugins:
	git submodule deinit --force plugin
