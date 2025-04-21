import { ReplyControllerApi, Configuration } from '../generated/replies';

class ReplyApiClient {
    private static instance: ReplyControllerApi | null = null;
    
    private constructor() {}
    
    public static getInstance(): ReplyControllerApi {
        if (!this.instance) {
            const config = new Configuration({
                basePath: import.meta.env.VITE_API_BASE_URL || window.location.origin,
                credentials: 'include',
            });
            this.instance = new ReplyControllerApi(config);
        }
        return this.instance;
    }
}

export const replyApi = ReplyApiClient.getInstance();