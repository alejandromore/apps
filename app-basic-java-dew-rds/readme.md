mvn clean package -DskipTests

mvn spring-boot:run

wsl --distribution docker-desktop
docker image ls
docker build -t app-basic-java:1.0 .
docker run -p 8080:8080 app-basic-java:1.0


mkdir $HOME\.kube
Copy-Item cce-test-local-kubeconfig.yaml $HOME\.kube\config
kubectl config view
kubectl get nodes
kubectl get pods --all-namespaces
kubectl get deployments

kubectl apply -f deployment.yaml
kubectl get pods
kubectl apply -f service.yaml
kubectl get svc

# Login en SWR
docker login -u la-south-2@HST3WPDWCCZQLLHYZ2KV -p 59cf40e42292cd7caa7dc79bbee6a9bed269e530392d3ec75b7203247ba1dd26 swr.la-south-2.myhuaweicloud.com
# Subir la imagen a Huawei SWR (Software Repository for Containers)
docker tag app-basic-java:1.0 la-south-2.swr.myhuaweicloud.com/cce-jenkins-integration-organization/app-basic-java:1.0
docker push la-south-2.swr.myhuaweicloud.com/cce-jenkins-integration-organization/app-basic-java:1.0

3️⃣ Configurar kubectl para tu cluster CCE
hcloud cce cluster kubeconfig --cluster-id <CCE_CLUSTER_ID> --file ./kubeconfig
export KUBECONFIG=./kubeconfig
kubectl get nodes