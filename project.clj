(defproject fmaas "0.1.0-SNAPSHOT"
  :description "Floppy Music as a Service"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
  				 [cheshire "5.5.0"]
  				 [compojure "1.4.0"]
  				 [http-kit "2.1.19"]
  				 [com.moppy/moppydesk "1.0.0"]
                                 [nrjavaserial/nrjavaserial "3.9.3"]
  				 [javax.servlet/servlet-api "2.5"]
  				 [ring/ring-devel "1.4.0"]
  				 [ring/ring-core "1.4.0"]]
  :main fmaas.core
  :target-path "target/%s"
  :profiles {:dev
             {:dependencies
              [[javax.servlet/servlet-api "2.5"]]}}
  :aot [fmaas.core])
