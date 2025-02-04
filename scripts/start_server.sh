#!/bin/bash

# 배포 디렉토리 설정
DEPLOY_PATH="/home/ubuntu/spring_server/"

LOG_FILE="${DEPLOY_PATH}deploy.log"
ERROR_LOG_FILE="${DEPLOY_PATH}deploy_err.log"
S3_BUCKET="kongju-s3-bucket"

echo ">>> 배포 스크립트 시작" >> $LOG_FILE

# S3에서 application-prod.yml 다운로드
echo ">>> S3에서 application-prod.yml 다운로드" >> $LOG_FILE
aws s3 cp s3://${S3_BUCKET}/application-prod.yml ${DEPLOY_PATH}
if [ $? -ne 0 ]; then
  echo ">>> application-prod.yml 다운로드 실패" >> $LOG_FILE
  exit 1
fi
echo ">>> application-prod.yml 다운로드 완료" >> $LOG_FILE

# 빌드된 JAR 파일 경로 및 이름 (실행 가능한 JAR만 선택)
BUILD_JAR=$(ls ${DEPLOY_PATH}build/libs/*SNAPSHOT.jar | grep -v plain)
JAR_NAME=$(basename $BUILD_JAR)
echo ">>> build 파일명: $JAR_NAME" >> $LOG_FILE

# JAR 파일 복사
echo ">>> build 파일 복사" >> $LOG_FILE
cp $BUILD_JAR $DEPLOY_PATH

# 현재 실행 중인 애플리케이션 PID 확인 및 종료
echo ">>> 현재 실행중인 애플리케이션 pid 확인" >> $LOG_FILE
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]; then
  echo ">>> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> $LOG_FILE
else
  echo ">>> kill -15 $CURRENT_PID" >> $LOG_FILE
  kill -15 $CURRENT_PID
  sleep 5
fi

# JAR 파일 배포 및 실행
DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo ">>> DEPLOY_JAR 배포" >> $LOG_FILE
nohup java -Dspring.config.location=${DEPLOY_PATH}application-prod.yml \
-Dspring.profiles.active=prod \
-jar ${DEPLOY_PATH}kongju-0.0.1-SNAPSHOT.jar >> $LOG_FILE 2>> $ERROR_LOG_FILE &


echo ">>> 배포 완료. 애플리케이션이 prod 프로파일로 실행되었습니다." >> $LOG_FILE