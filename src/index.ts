import { registerPlugin } from '@capacitor/core';

import type { VeriffPluginPlugin } from './definitions';

const VeriffPlugin = registerPlugin<VeriffPluginPlugin>('VeriffPlugin', {
  web: () => import('./web').then(m => new m.VeriffPluginWeb()),
});

export * from './definitions';
export { VeriffPlugin };
