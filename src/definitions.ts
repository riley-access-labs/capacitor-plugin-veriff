
export interface VeriffPluginPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;

  start(sessionToken: string, configuration: {themeColor: string}): Promise<{ message: string, status: string }>;
}
