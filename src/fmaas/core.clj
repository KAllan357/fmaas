(ns fmaas.core
  (:gen-class)
  (:use [compojure.route :only [files not-found]]
  		[compojure.handler :only [site]]
  		[compojure.core :only [defroutes GET POST DELETE ANY context]]
  		org.httpkit.server)
  (:require [fmaas.endpoints :as endpoints]
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
    (run-server handler {:port 8080})))

