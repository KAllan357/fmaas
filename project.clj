(defproject fmaas "0.1.0-SNAPSHOT"
  :description "Floppy Music as a Service"
  :url "http://example.com/FIXME"
  :license {:name "Apache License Version 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler fmaas.core/api
         :port 8080
         :nrepl {:start? true}}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [compojure "1.5.1"]
                 [http-kit "2.1.19"]
                 [nrjavaserial/nrjavaserial "3.9.3"]
                 [jarohen/nomad "0.7.2"]
                 [javax.servlet/servlet-api "2.5"]
                 [ring/ring-core "1.5.0"]
                 [ring/ring-defaults "0.2.1"]
                 [ring.middleware.logger "0.5.0"]
                 [ring/ring-json "0.4.0"]]
  :main fmaas.core
  :target-path "target/%s"
  :profiles {:dev
             {:dependencies
              [[javax.servlet/servlet-api "2.5"]]}
             :jvm-opts ["-Dnomad.env=dev"]}
  :resource-paths ["src/resources"]
  :aot [fmaas.core])
