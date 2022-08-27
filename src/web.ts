import { WebPlugin } from '@capacitor/core';

import type { VeriffPluginPlugin } from './definitions';

export class VeriffPluginWeb extends WebPlugin implements VeriffPluginPlugin {
  start(params: {
    sessionUrl: string;
    configuration: { themeColor: string };
  }): Promise<{ message: string; status: string }> {
    console.log('Params: ', params);
    throw new Error('Method not implemented.');
  }
}
