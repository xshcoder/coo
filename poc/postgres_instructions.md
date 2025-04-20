# init sql naming convention
init sql should be executed in order, order is defined by the number prefix of the file name, e.g. 0001_init.sql, 0002_init.sql, etc.

# how to reset database for docker desktop
docker desktop persistent volume is located inside C:\Users\<you>\AppData\Local\Docker\wsl\disk\docker_data.vhdx file

if you want to reset database, you can delete the postgres data files inside docker container
docker exec -it postgres /bin/bash
rm -rf /var/lib/postgresql/dat/*

then restart the docker container
docker restart postgres
or
delete postgres-service container and start it again

# how to mount vhdx
stop docker desktop
Open Disk Management->Action-> Attach VHD and navigate to the .VHDX file. then assign a letter to it.

