(ns fmaas.core
  (:gen-class)
  (:require [fmaas.endpoints :as endpoints]
            [fmaas.config.config :as config]
            [compojure.core :refer [defroutes]]
            [org.httpkit.server :refer [run-server]]
            [ring.middleware.logger :as logger]
            [ring.middleware.defaults :refer :all]
            [ring.middleware.json :as json]))

(defroutes all-routes
  (endpoints/all))

(def api
  (let [handler all-routes]
    (-> handler
        (wrap-defaults (assoc-in api-defaults [:responses :content-types] false))
        (logger/wrap-with-logger)
        (json/wrap-json-response))))

(defn -main [& args]
  "Main function for starting the server as an uberjar."
  (run-server api {:port (:port config/get-config)}))
