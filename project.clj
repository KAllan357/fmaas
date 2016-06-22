(defproject fmaas "0.1.0-SNAPSHOT"
  :description "Floppy Music as a Service"
  :url "http://example.com/FIXME"
  :license {:name "Apache License Version 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :plugins [[lein-localrepo "0.5.3"]]
  :dependencies [[org.clojure/clojure "1.8.0"]
  				 [cheshire "5.5.0"]
  				 [compojure "1.4.0"]
  				 [http-kit "2.1.19"]
  				 [com.moppy/moppydesk "1.0.0"]
                                 [nrjavaserial/nrjavaserial "3.9.3"]
                                 [jarohen/nomad "0.7.2"]
  				 [javax.servlet/servlet-api "2.5"]
  				 [ring/ring-devel "1.4.0"]
  				 [ring/ring-core "1.4.0"]]
  :main fmaas.core
  :target-path "target/%s"
  :profiles {:dev
             {:dependencies
              [[javax.servlet/servlet-api "2.5"]]}
             :jvm-opts ["-Dnomad.env=dev"]}
  :resource-paths ["src/resources"]
  :aot [fmaas.core])
