#!/bin/bash

# -----------------------------
# [ Spring Boot 자동 배포 + 백업 + 폴더 자동 생성 ]
# -----------------------------

# 1. 변수 설정
PEM_PATH=~/springboot_keypair.pem                # PEM 키 파일 경로 (로컬 기준)
SERVER_USER=ubuntu                              # EC2 사용자명
SERVER_IP=3.37.33.160                         # EC2 퍼블릭 IP
DEPLOY_PATH=/home/ubuntu/deploy                 # ⭐️ EC2 서버 절대경로로 명시
JAR_NAME=oneend-0.0.1-SNAPSHOT.jar              # JAR 파일명
BACKUP_PATH=$DEPLOY_PATH/backup                 # 백업 경로 (절대 경로)

# 2. 빌드 시작
echo "✅ 1. 빌드 시작..."
./gradlew clean bootJar

if [ $? -ne 0 ]; then
  echo "❌ 빌드 실패. 중단합니다."
  exit 1
fi
echo "🎉 빌드 완료!"

# 3. EC2 서버에 배포 경로 및 백업 폴더 자동 생성
echo "✅ 2. 서버에 배포 경로 확인 및 생성..."
ssh -i $PEM_PATH $SERVER_USER@$SERVER_IP <<EOF
  echo "🚀 서버 접속 완료"
  mkdir -p $DEPLOY_PATH
  mkdir -p $BACKUP_PATH
  echo "📂 배포 및 백업 경로 준비 완료"
EOF

# 4. 기존 JAR 백업
echo "✅ 3. 기존 JAR 백업..."
ssh -i $PEM_PATH $SERVER_USER@$SERVER_IP <<EOF
  if [ -f $DEPLOY_PATH/$JAR_NAME ]; then
    TIMESTAMP=\$(date +%Y%m%d_%H%M%S)
    mv $DEPLOY_PATH/$JAR_NAME $BACKUP_PATH/${JAR_NAME}_\$TIMESTAMP
    echo "📦 기존 JAR 백업 완료: ${JAR_NAME}_\$TIMESTAMP"
  else
    echo "🟢 기존 JAR 없음, 백업 생략"
  fi
EOF

# 5. 새 JAR 파일 업로드 (이제 제대로 동작)
echo "✅ 4. 새 JAR 파일 업로드..."
scp -i $PEM_PATH build/libs/$JAR_NAME $SERVER_USER@$SERVER_IP:$DEPLOY_PATH/

if [ $? -ne 0 ]; then
  echo "❌ JAR 업로드 실패. 중단합니다."
  exit 1
fi
echo "🎉 JAR 업로드 완료!"

# 6. 서버 재시작 (기존 프로세스 종료 후 새 JAR 실행)
echo "✅ 5. 서버 애플리케이션 재시작..."
ssh -i $PEM_PATH $SERVER_USER@$SERVER_IP <<EOF
  echo "🚀 서버 접속 완료 (재시작 준비)"
  
  # 기존 애플리케이션 종료
  PID=\$(pgrep -f $JAR_NAME)
  if [ -n "\$PID" ]; then
    kill -9 \$PID
    echo "🛑 기존 애플리케이션 종료 (PID: \$PID)"
  else
    echo "🟢 실행 중인 애플리케이션 없음"
  fi

  # 애플리케이션 재시작
  nohup java -jar $DEPLOY_PATH/$JAR_NAME > $DEPLOY_PATH/nohup.out 2>&1 &
  echo "🚀 새 애플리케이션 실행 완료!"
EOF

echo "🎉 전체 배포 프로세스 완료!"
