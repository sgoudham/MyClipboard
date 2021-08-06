pipeline {
    agent {
        dockerfile {
            args '-u root'
        }
    }

    environment {
        CODECOV_TOKEN = credentials('ea3e54d7-7f4f-40ed-af70-d8132e1c405b')

        GPG_SECRET_KEY = credentials('4dbfd4ed-bba4-44e0-8410-fbce1a9bba73')
        GPG_OWNER_TRUST = credentials('8703bbe8-c099-481f-8337-1dce32d51771')
    }

    stages {
        stage("Import GPG Keys") {
            when {
                branch 'release'
            }
            steps {
                sh 'gpg --batch --import $GPG_SECRET_KEY'
                sh 'gpg --import-ownertrust $GPG_OWNER_TRUST'
            }
        }
        stage("Build") {
            steps {
                withCredentials([file(credentialsId: '076a36e8-d448-46fc-af11-7e7181a6cb99', variable: 'MAVEN_SETTINGS')]) {
                    sh 'mvn -s $MAVEN_SETTINGS -B -DskipTests clean package'
                }
            }
        }
        stage("Test") {
            steps {
                wrap([$class: 'Xvnc']) {
                    sh "mvn test"
                }
            }
            post {
                success {
                    echo "Generating Test Report..."
                    publishCoverage adapters: [jacocoAdapter('target/site/jacoco/jacoco.xml')]

                    echo "Sending Report to CodeCov..."
                    sh '''#!/bin/bash
                          bash <(curl -s https://codecov.io/bash) -t $CODECOV_TOKEN || echo "Codecov did not collect coverage reports"
                       '''
                }
            }
        }
        stage("Deploy To OSSRH") {
            when {
                branch 'release'
            }
            steps {
                withCredentials([file(credentialsId: '076a36e8-d448-46fc-af11-7e7181a6cb99', variable: 'MAVEN_SETTINGS')]) {
                    sh 'mvn -s $MAVEN_SETTINGS -B -DskipTests clean deploy'
                }
            }
            post {
                success {
                    echo "Archiving Artifacts"
                    archiveArtifacts artifacts: 'target/*.jar'
                }
            }
        }
    }

    post {
        success {
            echo "I'm Feeling Swag!"
        }
        failure {
            echo 'Not Very Swag :('
        }
        cleanup {
            cleanWs()
        }
    }
}