(ns fmaas.core
  (:gen-class)
  (:require [fmaas.endpoints :as endpoints]
            [fmaas.config.config :as config]
            [compojure.core :refer [defroutes]]
            [org.httpkit.server :refer [run-server]]
            [ring.middleware.logger :as logger]
            [ring.middleware.defaults :refer :all]))

(defroutes all-routes
  (endpoints/all))

(def api
  (let [handler all-routes]
    (-> handler
        (wrap-defaults api-defaults)
        (logger/wrap-with-logger))))

(defn -main [& args]
  "Main function for starting the server as an uberjar."
  (run-server api {:port (:port config/get-config)}))
