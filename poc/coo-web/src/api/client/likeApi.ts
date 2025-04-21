import { LikeControllerApi, Configuration } from '../generated/likes';

class LikeApiClient {
    private static instance: LikeControllerApi | null = null;
    
    private constructor() {}
    
    public static getInstance(): LikeControllerApi {
        if (!this.instance) {
            const config = new Configuration({
                basePath: import.meta.env.VITE_API_BASE_URL || window.location.origin,
                credentials: 'include',
            });
            this.instance = new LikeControllerApi(config);
        }
        return this.instance;
    }
}

export const likeApi = LikeApiClient.getInstance();