## how to change VITE_API_BASE_URL
import { BASE_PATH as USER_BASE_PATH } from './generated/users';

export const initializeAPI = () => {
  // request against 8080 port will be proxied to 80 port, it is defined in vite.config.ts
  // VITE_API_BASE_URL is used in docker image, docker image need to have same port as coo-web - 8080,
  // otherwise api request will not be proxied
  // window.location.origin is used for local development using >npm run dev
  const baseUrl = import.meta.env.VITE_API_BASE_URL || `${window.location.origin}`;
  USER_BASE_PATH = baseUrl;
};

## how to create a custom configuration for openapi generated client
// Create a custom configuration
const customConfig = new runtime.Configuration({
    basePath: 'http://your-custom-domain:port', // Override the default base path
    headers: {
        'Custom-Header': 'value',  // Add custom headers
        'Authorization': 'Bearer your-token'
    },
    credentials: 'include', // Configure credentials behavior
    middleware: [
        {
            pre: async ({ url, init }) => {
                // Pre-request middleware
                return { url, init };
            },
            post: async (response) => {
                // Post-response middleware
                return response;
            }
        }
    ]
});

// Create UserControllerApi instance with custom configuration
const userApi = new UserControllerApi(customConfig);

## setup nginx proxy for running coo-web in docker
host.docker.internal is a special DNS name that allows containers to access the host machine
This ensures requests are properly forwarded to port 80 on your host machine where the Kubernetes ingress is running
proxy_pass http://host.docker.internal/api/;
