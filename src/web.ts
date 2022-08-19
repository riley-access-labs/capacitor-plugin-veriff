import { WebPlugin } from '@capacitor/core';

import type { VeriffPluginPlugin } from './definitions';

export class VeriffPluginWeb extends WebPlugin implements VeriffPluginPlugin {
  start(sessionToken: string, configuration: { themeColor: string; }): Promise<{ message: string; status: string; }> {
    console.log("token", sessionToken);
    console.log("config", configuration);
    throw new Error('Method not implemented.');
  }

  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
