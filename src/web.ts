import { WebPlugin } from '@capacitor/core';

import type { VeriffPluginPlugin } from './definitions';

export class VeriffPluginWeb extends WebPlugin implements VeriffPluginPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
