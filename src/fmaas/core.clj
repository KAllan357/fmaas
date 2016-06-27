(ns fmaas.core
  (:gen-class)
  (:require [fmaas.endpoints :as endpoints]
            [fmaas.config.config :as config]
            [compojure.core :refer [defroutes]]
            [compojure.handler :refer [site]]
            [org.httpkit.server :refer [run-server]]
            [ring.middleware.reload :as reload]))

(defn in-dev? []
	(not (nil? (System/getenv "FMAAS_DEV"))))

(defroutes all-routes
  (endpoints/all))

(defn -main [& args]
  "Main function for starting the server as an uberjar."
  (let [handler (if (in-dev?)
                  (reload/wrap-reload (site #'all-routes)) ;; only reload when dev
                  (site all-routes))]
    (run-server handler {:port (:port config/get-config)})))

