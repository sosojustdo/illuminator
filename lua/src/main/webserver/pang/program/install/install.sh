#!/usr/bin/env bash

BASE_DIR=/home/coupang/nginxinstall

pushd $BASE_DIR
tar -xvf nginx-1.10.2.tar
tar -xvf nginxmodule.tar
popd

pushd $BASE_DIR/nginxmodule/LuaJIT-2.1.0-beta2
make
make install PREFIX=/pang/program/luajit

popd

export LUAJIT_LIB=/pang/program/luajit/lib
export LUAJIT_INC=/pang/program/luajit/include/luajit-2.1

pushd $BASE_DIR/nginx-1.10.2
./configure --prefix=/pang/program/nginx-1.10.2 --sbin-path=sbin/nginx --conf-path=conf/nginx.conf --error-log-path=/pang/logs/nginx/error.log --http-log-path=/pang/logs/nginx/access.log --pid-path=/pang/logs/nginx/nginx.pid --lock-path=/pang/logs/nginx/nginx.lock --http-client-body-temp-path=/tmp/client_temp --http-proxy-temp-path=/tmp/proxy_temp --http-fastcgi-temp-path=/tmp/fastcgi_temp --http-uwsgi-temp-path=/tmp/uwsgi_temp --http-scgi-temp-path=/tmp/scgi_temp --user=nginx --group=coupang --with-http_realip_module --with-http_addition_module --with-http_sub_module --with-http_dav_module --with-http_flv_module --with-http_mp4_module --with-http_gunzip_module --with-http_gzip_static_module --with-http_random_index_module --with-http_secure_link_module --with-http_stub_status_module --with-http_auth_request_module --with-threads --with-stream --with-http_slice_module --with-http_v2_module --with-http_ssl_module --with-ipv6 --add-module=$BASE_DIR/nginxmodule/lua-nginx-module --add-module=$BASE_DIR/nginxmodule/ngx_devel_kit  --add-module=$BASE_DIR/nginxmodule/lua-upstream-nginx-module  --with-pcre=$BASE_DIR/nginxmodule/pcre-8.38 --with-zlib=$BASE_DIR/nginxmodule/zlib-1.2.8 --with-ld-opt="-Wl,-rpath,/pang/program/luajit/lib" --with-openssl=$BASE_DIR/nginxmodule/openssl-1.0.1u

make -j2
make install

popd