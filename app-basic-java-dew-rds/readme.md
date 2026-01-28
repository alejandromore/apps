mvn clean package -DskipTests

mvn spring-boot:run

wsl --distribution docker-desktop
wsl --list --verbose
wsl --shutdown

docker version
docker image ls

docker build -t app-basic-java:1.0 .
docker build --provenance=false --sbom=false -t app-basic-java:1.0 .
docker run -p 8080:8080 app-basic-java:1.0

#login swr remoto
docker login -u la-south-2@HST3WCLCQUZN69YR1Q7M -p 4a9d398241cd07ffc1c7d31359fa4cf0cdd98d279cde27d5cd8c7b5bd68d145f swr.la-south-2.myhuaweicloud.com
#tag
docker tag app-basic-java:1.0 swr.la-south-2.myhuaweicloud.com/cce-basic-app/app-basic-java:1.0
#push
docker push swr.la-south-2.myhuaweicloud.com/cce-basic-app/app-basic-java:1.0

mkdir -p ~/.kube
ls ~/.kube
cp cce-app-kubeconfig.yaml ~/.kube/config
cat ~/.kube/config

kubectl apply -f deployment.yaml
kubectl apply -f service.yaml

kubectl get nodes
kubectl get pods
kubectl get svc

kubectl describe pod app-basic-java-xxxx
kubectl logs app-basic-java-xxxx
kubectl describe pod app-basic-java-668dd6d96d-wflqz