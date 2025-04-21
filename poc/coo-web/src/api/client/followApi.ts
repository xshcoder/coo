import { FollowControllerApi, Configuration } from '../generated/follows';

class FollowApiClient {
    private static instance: FollowControllerApi | null = null;
    
    private constructor() {}
    
    public static getInstance(): FollowControllerApi {
        if (!this.instance) {
            const config = new Configuration({
                basePath: import.meta.env.VITE_API_BASE_URL || window.location.origin,
                credentials: 'include',
            });
            this.instance = new FollowControllerApi(config);
        }
        return this.instance;
    }
}

export const followApi = FollowApiClient.getInstance();