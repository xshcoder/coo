import { UserControllerApi, Configuration } from '../generated/users';

class UserApiClient {
    private static instance: UserControllerApi | null = null;
    
    private constructor() {}
    
    public static getInstance(): UserControllerApi {
        if (!this.instance) {
            console.log("VITE_API_BASE_URL", import.meta.env.VITE_API_BASE_URL);
            console.log("window.location.origin", window.location.origin);
            const config = new Configuration({
                // Add any custom configuration here
                basePath: import.meta.env.VITE_API_BASE_URL || window.location.origin,
                credentials: 'include',
            });
            this.instance = new UserControllerApi(config);
        }
        return this.instance;
    }
}

export const userApi = UserApiClient.getInstance();